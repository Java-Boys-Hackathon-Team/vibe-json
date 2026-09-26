import { LitElement, html, svg, css, nothing } from 'lit';

/*
 * Диаграмма workflow: стартеры, активити и переходы между ними.
 *
 * На вход приходит JSON схемы строкой (свойство workflow). Граф раскладывается
 * по слоям сверху вниз: слой узла - длина самого длинного пути до него от старта
 * без учёта обратных рёбер, поэтому циклы (повторы, ожидания) не ломают
 * раскладку и рисуются отдельной пунктирной дугой сбоку.
 */

const NODE_W = 212;
const NODE_H = 60;
const GAP_X = 32;
const GAP_Y = 64;
const PAD = 28;

const ICONS = {
  play: 'M7 4.5v15l12.5-7.5z',
  flag: 'M5 21V4m0 0h11l-2 4 2 4H5',
  layers: 'M12 3 2 8l10 5 10-5-10-5zM2 16l10 5 10-5M2 12l10 5 10-5',
  globe: 'M12 3a9 9 0 1 0 0 18 9 9 0 0 0 0-18zM3 12h18M12 3c2.5 2.7 3.8 5.7 3.8 9s-1.3 6.3-3.8 9c-2.5-2.7-3.8-5.7-3.8-9S9.5 5.7 12 3z',
  database: 'M4 6c0-1.7 3.6-3 8-3s8 1.3 8 3-3.6 3-8 3-8-1.3-8-3zm0 0v12c0 1.7 3.6 3 8 3s8-1.3 8-3V6M4 12c0 1.7 3.6 3 8 3s8-1.3 8-3',
  send: 'M22 2 11 13M22 2l-7 20-4-9-9-4 20-7z',
  shuffle: 'M16 3h5v5M4 20 21 3M21 16v5h-5M15 15l6 6M4 4l5 5',
  branch: 'M6 3v12M18 9a3 3 0 1 0 0-6 3 3 0 0 0 0 6zM6 21a3 3 0 1 0 0-6 3 3 0 0 0 0 6zM18 9a9 9 0 0 1-9 9',
  parallel: 'M4 12h5m0 0 5-6h6m-11 6 5 6h6',
  clock: 'M12 21a9 9 0 1 0 0-18 9 9 0 0 0 0 18zM12 7v5l3 2',
  inbox: 'M22 12h-6l-2 3h-4l-2-3H2M5.5 5.1 2 12v6a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2v-6l-3.5-6.9A2 2 0 0 0 16.8 4H7.2a2 2 0 0 0-1.7 1.1z',
  plus: 'M12 5v14M5 12h14',
  file: 'M14 3H6a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V9zM14 3v6h6',
  cloud: 'M17.5 19a4.5 4.5 0 1 0-1.4-8.8A6 6 0 1 0 6 16.9M6 19h11.5',
  mail: 'M4 5h16a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V6a1 1 0 0 1 1-1zm-1 1 9 7 9-7',
  alert: 'M12 9v4m0 4h.01M10.3 3.9 1.8 18a2 2 0 0 0 1.7 3h17a2 2 0 0 0 1.7-3L13.7 3.9a2 2 0 0 0-3.4 0z',
};

const TYPES = {
  start: { label: 'Старт', color: '#10b981', icon: 'play' },
  end: { label: 'Завершение', color: '#64748b', icon: 'flag' },
  missing: { label: 'Шаг не найден', color: '#ef4444', icon: 'alert' },
  workflow_call: { label: 'Вызов процесса', color: '#6366f1', icon: 'layers' },
  workflow: { label: 'Вызов процесса', color: '#6366f1', icon: 'layers' },
  rest_call: { label: 'REST-вызов', color: '#0ea5e9', icon: 'globe' },
  db_call: { label: 'Запрос в БД', color: '#f59e0b', icon: 'database' },
  send_to_rabbitmq: { label: 'Отправка в RabbitMQ', color: '#f97316', icon: 'send' },
  send_to_kafka: { label: 'Отправка в Kafka', color: '#8b5cf6', icon: 'send' },
  send_to_sap: { label: 'Отправка в SAP', color: '#2563eb', icon: 'send' },
  send_to_s3: { label: 'Выгрузка в S3', color: '#e11d48', icon: 'cloud' },
  xslt_transform: { label: 'XSLT-преобразование', color: '#14b8a6', icon: 'file' },
  transform: { label: 'Преобразование', color: '#14b8a6', icon: 'shuffle' },
  inject: { label: 'Задать данные', color: '#a855f7', icon: 'plus' },
  switch: { label: 'Ветвление', color: '#ec4899', icon: 'branch' },
  parallel: { label: 'Параллельно', color: '#d946ef', icon: 'parallel' },
  timer: { label: 'Таймер', color: '#eab308', icon: 'clock' },
  await_for_message: { label: 'Ждать сообщение', color: '#06b6d4', icon: 'inbox' },
};

const STARTERS = {
  rest_call: 'REST-запрос',
  kafka_consumer: 'Kafka',
  rabbitmq_consumer: 'RabbitMQ',
  sap_inbound: 'SAP',
  scheduler: 'Расписание',
  mail_consumer: 'Почта',
};

const typeMeta = (type) => TYPES[type] || { label: type || 'Шаг', color: '#64748b', icon: 'layers' };

const targetOf = (t) => {
  if (!t) return null;
  if (typeof t === 'string') return t;
  return t.next || t.transition || t.id || null;
};

class VjWorkflowDiagram extends LitElement {
  static properties = {
    workflow: { type: String },
    _zoom: { state: true },
    _fit: { state: true },
    _selected: { state: true },
    _width: { state: true },
  };

  constructor() {
    super();
    this.workflow = '';
    this._zoom = 1;
    this._fit = true;
    this._selected = null;
    this._width = 0;
    this._graph = null;
    this._error = null;
  }

  connectedCallback() {
    super.connectedCallback();
    this._resizeObserver = new ResizeObserver((entries) => {
      this._width = entries[0].contentRect.width;
    });
    this._resizeObserver.observe(this);
  }

  disconnectedCallback() {
    this._resizeObserver?.disconnect();
    super.disconnectedCallback();
  }

  willUpdate(changed) {
    if (changed.has('workflow')) {
      this._selected = null;
      this._error = null;
      this._graph = null;
      if (this.workflow) {
        try {
          this._graph = this._layout(this._buildGraph(JSON.parse(this.workflow)));
        } catch (e) {
          this._error = 'Не удалось построить диаграмму: схема не является корректным JSON.';
        }
      }
    }
  }

  _buildGraph(wf) {
    const activities = (wf && wf.compiled && Array.isArray(wf.compiled.activities)) ? wf.compiled.activities : [];
    const starters = (wf && wf.details && Array.isArray(wf.details.starters)) ? wf.details.starters : [];
    const nodes = new Map();
    const edges = [];

    const starterNames = starters.map((s) => STARTERS[s.type] || s.type).filter(Boolean);
    nodes.set('__start', {
      id: '__start',
      kind: 'start',
      title: 'Старт',
      subtitle: starterNames.length ? starterNames.join(', ') : 'стартер не задан',
      data: starters,
    });

    activities.forEach((a, i) => {
      const id = a.id || `activity-${i + 1}`;
      const innerType = a.workflowCall && a.workflowCall.workflowDef && a.workflowCall.workflowDef.type;
      const meta = typeMeta(a.type === 'workflow_call' && innerType && TYPES[innerType] ? innerType : a.type);
      nodes.set(id, {
        id,
        kind: a.type,
        visualType: a.type === 'workflow_call' && innerType && TYPES[innerType] ? innerType : a.type,
        title: meta.label,
        subtitle: a.description || id,
        data: a,
      });
    });

    const addEdge = (from, to, label, kind) => {
      if (!to) return;
      if (!nodes.has(to)) {
        nodes.set(to, { id: to, kind: 'missing', title: 'Шаг не найден', subtitle: to, data: null });
      }
      edges.push({ from, to, label, kind });
    };

    activities.forEach((a, i) => {
      const id = a.id || `activity-${i + 1}`;
      (a.dataConditions || []).forEach((c, idx) =>
        addEdge(id, targetOf(c.transition), c.conditionDescription || `условие ${idx + 1}`, 'cond'));
      addEdge(id, targetOf(a.defaultCondition && a.defaultCondition.transition), 'иначе', 'default');
      addEdge(id, targetOf(a.defaultTransition && a.defaultTransition.transition),
        (a.defaultTransition && a.defaultTransition.conditionDescription) || 'иначе', 'default');
      (a.branches || []).forEach((b, idx) => addEdge(id, targetOf(b), `ветка ${idx + 1}`, 'branch'));
      addEdge(id, targetOf(a.transition), a.type === 'parallel' ? 'после веток' : '', 'next');
    });

    const start = wf && wf.compiled && wf.compiled.start;
    let roots;
    if (start && nodes.has(start)) {
      roots = [start];
    } else {
      const targeted = new Set(edges.map((e) => e.to));
      roots = activities.map((a, i) => a.id || `activity-${i + 1}`).filter((id) => !targeted.has(id));
      if (!roots.length && activities.length) roots = [activities[0].id || 'activity-1'];
    }
    roots.forEach((r) => edges.unshift({ from: '__start', to: r, label: '', kind: 'next' }));

    // Шаги без исходящих переходов завершают процесс. Ветки parallel сюда не
    // относятся: после них управление возвращается в сам parallel.
    const branchTargets = new Set(edges.filter((e) => e.kind === 'branch').map((e) => e.to));
    const terminals = [...nodes.values()].filter((n) => n.id !== '__start' && n.kind !== 'missing'
      && !edges.some((e) => e.from === n.id) && !branchTargets.has(n.id));
    if (terminals.length || !activities.length) {
      nodes.set('__end', { id: '__end', kind: 'end', title: 'Завершение', subtitle: 'процесс окончен', data: null });
      if (!activities.length) edges.push({ from: '__start', to: '__end', label: '', kind: 'end' });
      terminals.forEach((t) => edges.push({ from: t.id, to: '__end', label: '', kind: 'end' }));
    }

    return { nodes, edges, name: wf && wf.name };
  }

  _layout(graph) {
    const { nodes, edges } = graph;
    const out = new Map([...nodes.keys()].map((id) => [id, []]));
    edges.forEach((e) => out.get(e.from).push(e));

    // Обратные рёбра находятся обходом в глубину от старта.
    const state = new Map();
    const visit = (id) => {
      state.set(id, 1);
      for (const e of out.get(id)) {
        const s = state.get(e.to);
        if (s === 1) e.back = true;
        else if (!s) visit(e.to);
      }
      state.set(id, 2);
    };
    visit('__start');
    [...nodes.keys()].forEach((id) => { if (!state.get(id)) visit(id); });

    // Слой узла - самый длинный путь до него по прямым рёбрам.
    const layer = new Map([...nodes.keys()].map((id) => [id, 0]));
    const indeg = new Map([...nodes.keys()].map((id) => [id, 0]));
    edges.filter((e) => !e.back).forEach((e) => indeg.set(e.to, indeg.get(e.to) + 1));
    const queue = [...nodes.keys()].filter((id) => indeg.get(id) === 0);
    while (queue.length) {
      const id = queue.shift();
      for (const e of out.get(id)) {
        if (e.back) continue;
        layer.set(e.to, Math.max(layer.get(e.to), layer.get(id) + 1));
        indeg.set(e.to, indeg.get(e.to) - 1);
        if (indeg.get(e.to) === 0) queue.push(e.to);
      }
    }
    if (nodes.has('__end')) {
      const max = Math.max(...[...layer.entries()].filter(([id]) => id !== '__end').map(([, l]) => l));
      layer.set('__end', max + 1);
    }

    const layers = [];
    [...nodes.keys()].forEach((id) => {
      const l = layer.get(id);
      (layers[l] = layers[l] || []).push(id);
    });

    // Порядок внутри слоя - по среднему положению родителей: так меньше пересечений.
    const order = new Map();
    layers.forEach((ids, l) => {
      if (l > 0) {
        const bary = (id) => {
          const parents = edges.filter((e) => e.to === id && !e.back && order.has(e.from)).map((e) => order.get(e.from));
          return parents.length ? parents.reduce((a, b) => a + b, 0) / parents.length : Number.MAX_SAFE_INTEGER;
        };
        ids.sort((a, b) => bary(a) - bary(b));
      }
      ids.forEach((id, i) => order.set(id, i - (ids.length - 1) / 2));
    });

    // Обратные рёбра огибают узлы справа - под них нужен запас по ширине.
    const loopSpace = edges.some((e) => e.back) ? 110 : 0;
    const maxCount = Math.max(...layers.map((ids) => (ids ? ids.length : 0)));
    const width = PAD * 2 + maxCount * NODE_W + (maxCount - 1) * GAP_X + loopSpace;
    const height = PAD * 2 + layers.length * NODE_H + (layers.length - 1) * GAP_Y;
    const cx = (width - loopSpace) / 2;

    const pos = new Map();
    layers.forEach((ids, l) => {
      (ids || []).forEach((id) => {
        pos.set(id, {
          x: cx + order.get(id) * (NODE_W + GAP_X) - NODE_W / 2,
          y: PAD + l * (NODE_H + GAP_Y),
          layer: l,
        });
      });
    });

    return { ...graph, pos, width, height };
  }

  _edgePath(e, pos) {
    const a = pos.get(e.from);
    const b = pos.get(e.to);
    if (e.back || b.y <= a.y) {
      const sx = a.x + NODE_W;
      const sy = a.y + NODE_H / 2;
      const tx = b.x + NODE_W;
      const ty = b.y + NODE_H / 2;
      const bulge = Math.max(sx, tx) + 44 + Math.abs(sy - ty) * 0.08;
      return { d: `M${sx} ${sy} C${bulge} ${sy}, ${bulge} ${ty}, ${tx + 4} ${ty}`, lx: bulge - 12, ly: (sy + ty) / 2 };
    }
    const sx = a.x + NODE_W / 2;
    const sy = a.y + NODE_H;
    const tx = b.x + NODE_W / 2;
    const ty = b.y - 4;
    const dy = (ty - sy) / 2;
    return {
      d: `M${sx} ${sy} C${sx} ${sy + dy}, ${tx} ${ty - dy}, ${tx} ${ty}`,
      lx: (sx + tx) / 2,
      ly: (sy + ty) / 2,
    };
  }

  _scale() {
    if (!this._graph) return 1;
    if (this._fit) {
      const available = Math.max(this._width - 16, 200);
      return Math.min(1, available / this._graph.width);
    }
    return this._zoom;
  }

  _zoomBy(delta) {
    const current = this._scale();
    this._fit = false;
    this._zoom = Math.min(2, Math.max(0.3, Math.round((current + delta) * 10) / 10));
  }

  _select(node) {
    this._selected = this._selected && this._selected.id === node.id ? null : node;
  }

  render() {
    if (this._error) {
      return html`<div class="message">${this._error}</div>`;
    }
    if (!this._graph) {
      return nothing;
    }
    const g = this._graph;
    const scale = this._scale();
    const stepCount = [...g.nodes.values()].filter((n) => !['start', 'end', 'missing'].includes(n.kind)).length;

    return html`
      <div class="toolbar">
        <span class="legend">${stepCount} ${this._plural(stepCount, 'шаг', 'шага', 'шагов')} · нажмите на узел, чтобы увидеть параметры</span>
        <div class="zoom">
          <button title="Уменьшить" @click=${() => this._zoomBy(-0.1)}>−</button>
          <button class=${this._fit ? 'active' : ''} title="Вписать по ширине" @click=${() => { this._fit = true; }}>
            ${Math.round(scale * 100)}%
          </button>
          <button title="Увеличить" @click=${() => this._zoomBy(0.1)}>+</button>
        </div>
      </div>
      <div class="viewport">
        <div class="canvas" style="width:${g.width * scale}px;height:${g.height * scale}px">
          <div class="stage" style="width:${g.width}px;height:${g.height}px;transform:scale(${scale})">
            <svg class="edges" width=${g.width} height=${g.height} viewBox="0 0 ${g.width} ${g.height}">
              <defs>
                <marker id="arrow" viewBox="0 0 10 10" refX="8" refY="5" markerWidth="7" markerHeight="7" orient="auto-start-reverse">
                  <path d="M0 0 10 5 0 10z" class="arrow-head"></path>
                </marker>
                <marker id="arrow-accent" viewBox="0 0 10 10" refX="8" refY="5" markerWidth="7" markerHeight="7" orient="auto-start-reverse">
                  <path d="M0 0 10 5 0 10z" class="arrow-head accent"></path>
                </marker>
              </defs>
              ${g.edges.map((e) => this._renderEdge(e, g.pos))}
            </svg>
            ${[...g.nodes.values()].map((n) => this._renderNode(n, g.pos.get(n.id)))}
          </div>
        </div>
      </div>
      ${this._selected ? this._renderInspector(this._selected) : nothing}
    `;
  }

  _renderEdge(e, pos) {
    const { d, lx, ly } = this._edgePath(e, pos);
    const selected = this._selected && (this._selected.id === e.from || this._selected.id === e.to);
    const cls = `edge ${e.kind} ${e.back ? 'back' : ''} ${selected ? 'selected' : ''}`;
    const label = e.label ? String(e.label) : '';
    const text = label.length > 26 ? `${label.slice(0, 25)}…` : label;
    const w = text.length * 6.2 + 14;
    return svg`
      <path class=${cls} d=${d} marker-end=${selected ? 'url(#arrow-accent)' : 'url(#arrow)'}></path>
      ${text ? svg`
        <g class="edge-label ${e.kind}" transform="translate(${lx - w / 2} ${ly - 10})">
          <title>${label}</title>
          <rect width=${w} height="20" rx="10"></rect>
          <text x=${w / 2} y="14">${text}</text>
        </g>` : nothing}
    `;
  }

  _renderNode(n, p) {
    const meta = n.kind === 'start' || n.kind === 'end' || n.kind === 'missing' ? TYPES[n.kind] : typeMeta(n.visualType);
    const selected = this._selected && this._selected.id === n.id;
    const special = n.kind === 'start' || n.kind === 'end';
    return html`
      <div class="node ${special ? 'special' : ''} ${n.kind === 'missing' ? 'missing' : ''} ${selected ? 'selected' : ''}"
           style="left:${p.x}px;top:${p.y}px;--c:${meta.color};animation-delay:${p.layer * 60}ms"
           title=${n.subtitle}
           @click=${() => this._select(n)}>
        <div class="icon">
          <svg viewBox="0 0 24 24"><path d=${ICONS[meta.icon] || ICONS.layers}></path></svg>
        </div>
        <div class="text">
          <div class="title">${n.title}</div>
          <div class="subtitle">${n.subtitle}</div>
        </div>
      </div>
    `;
  }

  _renderInspector(n) {
    const json = n.data == null ? null : JSON.stringify(n.data, null, 2);
    return html`
      <div class="inspector">
        <div class="inspector-head">
          <div>
            <div class="inspector-title">${n.title}</div>
            <div class="inspector-id">${n.kind === 'start' ? 'стартеры процесса' : n.id}</div>
          </div>
          <button class="close" title="Закрыть" @click=${() => { this._selected = null; }}>✕</button>
        </div>
        ${json ? html`<pre>${json}</pre>` : html`<div class="inspector-empty">Нет параметров</div>`}
      </div>
    `;
  }

  _plural(n, one, few, many) {
    const m10 = n % 10;
    const m100 = n % 100;
    if (m10 === 1 && m100 !== 11) return one;
    if (m10 >= 2 && m10 <= 4 && (m100 < 12 || m100 > 14)) return few;
    return many;
  }

  static styles = css`
    :host {
      display: flex;
      flex-direction: column;
      position: relative;
      min-height: 0;
      font-family: var(--lumo-font-family);
      color: var(--vj-text, #16162a);
    }

    .message {
      padding: 1.5rem;
      color: var(--lumo-error-text-color, #dc2626);
      font-size: .9rem;
    }

    .toolbar {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: .5rem;
      padding: .25rem .25rem .6rem;
    }

    .legend {
      font-size: .78rem;
      color: var(--vj-text-3, #8a8aa3);
    }

    .zoom {
      display: inline-flex;
      border: 1px solid var(--vj-border, rgba(0, 0, 0, .1));
      border-radius: 9px;
      overflow: hidden;
      background: var(--vj-surface, #fff);
    }

    .zoom button {
      border: 0;
      background: transparent;
      color: var(--vj-text-2, #555);
      font: 500 .78rem/1 var(--lumo-font-family);
      min-width: 30px;
      height: 28px;
      padding: 0 .5rem;
      cursor: pointer;
    }

    .zoom button:hover {
      background: var(--vj-surface-2, #f3f3f8);
      color: var(--vj-text, #111);
    }

    .zoom button.active {
      color: var(--lumo-primary-text-color, #6d4df0);
    }

    .viewport {
      flex: 1;
      min-height: 0;
      overflow: auto;
      border-radius: 14px;
      border: 1px solid var(--vj-border, rgba(0, 0, 0, .1));
      background-color: var(--vj-surface-2, #f6f6fb);
      background-image: var(--vj-dot-grid);
      background-size: 18px 18px;
    }

    .canvas {
      position: relative;
      margin: 0 auto;
    }

    .stage {
      position: absolute;
      top: 0;
      left: 0;
      transform-origin: 0 0;
    }

    .edges {
      position: absolute;
      inset: 0;
      overflow: visible;
    }

    .edge {
      fill: none;
      stroke: var(--vj-text-3, #9a9ab0);
      stroke-width: 1.6;
      opacity: .75;
      transition: stroke .2s, opacity .2s;
    }

    .edge.cond { stroke: #ec4899; }
    .edge.branch { stroke: #d946ef; }
    .edge.end { stroke-dasharray: 4 5; opacity: .5; }
    .edge.back { stroke-dasharray: 6 5; stroke: #f59e0b; }
    .edge.selected {
      stroke: var(--lumo-primary-color, #7c5cf5);
      stroke-width: 2.4;
      opacity: 1;
    }

    .arrow-head { fill: var(--vj-text-3, #9a9ab0); }
    .arrow-head.accent { fill: var(--lumo-primary-color, #7c5cf5); }

    .edge-label rect {
      fill: var(--vj-surface, #fff);
      stroke: var(--vj-border-strong, rgba(0, 0, 0, .15));
    }

    .edge-label text {
      font: 500 10.5px var(--lumo-font-family);
      fill: var(--vj-text-2, #555);
      text-anchor: middle;
    }

    .edge-label.cond text { fill: #db2777; }
    .edge-label.branch text { fill: #c026d3; }

    .node {
      position: absolute;
      box-sizing: border-box;
      width: ${NODE_W}px;
      height: ${NODE_H}px;
      display: flex;
      align-items: center;
      gap: .7rem;
      padding: 0 .8rem 0 .65rem;
      border-radius: 14px;
      background: var(--vj-surface, #fff);
      border: 1px solid var(--vj-border-strong, rgba(0, 0, 0, .14));
      box-shadow: var(--vj-shadow-s);
      cursor: pointer;
      transition: transform .15s ease, box-shadow .2s ease, border-color .2s ease;
      animation: pop .35s cubic-bezier(.2, .8, .2, 1) both;
    }

    .node::before {
      content: '';
      position: absolute;
      left: -1px;
      top: 12px;
      bottom: 12px;
      width: 3px;
      border-radius: 0 3px 3px 0;
      background: var(--c);
    }

    .node:hover {
      transform: translateY(-2px);
      box-shadow: var(--vj-shadow-m);
      border-color: color-mix(in srgb, var(--c) 55%, transparent);
    }

    .node.selected {
      border-color: var(--c);
      box-shadow: 0 0 0 4px color-mix(in srgb, var(--c) 22%, transparent), var(--vj-shadow-m);
    }

    .node.special {
      border-radius: 999px;
      background: color-mix(in srgb, var(--c) 12%, var(--vj-surface, #fff));
      border-color: color-mix(in srgb, var(--c) 45%, transparent);
    }

    .node.special::before { display: none; }

    .node.missing {
      border-style: dashed;
      border-color: var(--c);
    }

    @keyframes pop {
      from { opacity: 0; transform: translateY(8px) scale(.97); }
      to { opacity: 1; transform: none; }
    }

    .icon {
      flex-shrink: 0;
      width: 34px;
      height: 34px;
      border-radius: 10px;
      display: grid;
      place-items: center;
      background: color-mix(in srgb, var(--c) 15%, transparent);
      color: var(--c);
    }

    .node.special .icon {
      border-radius: 50%;
      background: var(--c);
      color: #fff;
    }

    .icon svg {
      width: 18px;
      height: 18px;
      fill: none;
      stroke: currentColor;
      stroke-width: 2;
      stroke-linecap: round;
      stroke-linejoin: round;
    }

    .node.special .icon svg path[d^="M7 4.5"] { fill: currentColor; stroke: none; }

    .text {
      min-width: 0;
      display: flex;
      flex-direction: column;
      gap: 2px;
    }

    .title {
      font-size: .82rem;
      font-weight: 600;
      color: var(--vj-text, #16162a);
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .subtitle {
      font-size: .72rem;
      color: var(--vj-text-3, #8a8aa3);
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .inspector {
      position: absolute;
      left: 12px;
      right: 12px;
      bottom: 12px;
      max-height: 45%;
      display: flex;
      flex-direction: column;
      border-radius: 14px;
      background: var(--vj-surface, #fff);
      border: 1px solid var(--vj-border-strong, rgba(0, 0, 0, .14));
      box-shadow: var(--vj-shadow-l);
      animation: pop .25s ease both;
      overflow: hidden;
    }

    .inspector-head {
      display: flex;
      align-items: flex-start;
      justify-content: space-between;
      padding: .8rem 1rem .6rem;
      border-bottom: 1px solid var(--vj-border, rgba(0, 0, 0, .08));
    }

    .inspector-title {
      font-weight: 600;
      font-size: .9rem;
    }

    .inspector-id {
      font-size: .75rem;
      color: var(--vj-text-3, #8a8aa3);
      font-family: ui-monospace, SFMono-Regular, Menlo, Consolas, monospace;
    }

    .close {
      border: 0;
      background: transparent;
      color: var(--vj-text-3, #888);
      cursor: pointer;
      font-size: .9rem;
      width: 28px;
      height: 28px;
      border-radius: 8px;
    }

    .close:hover {
      background: var(--vj-surface-2, #f3f3f8);
      color: var(--vj-text, #111);
    }

    pre {
      margin: 0;
      padding: .8rem 1rem 1rem;
      overflow: auto;
      font: .76rem/1.55 ui-monospace, SFMono-Regular, Menlo, Consolas, monospace;
      color: var(--vj-text-2, #444);
      background: var(--vj-code-bg, #f6f6fb);
    }

    .inspector-empty {
      padding: 1rem;
      font-size: .82rem;
      color: var(--vj-text-3, #888);
    }
  `;
}

customElements.define('vj-workflow-diagram', VjWorkflowDiagram);
