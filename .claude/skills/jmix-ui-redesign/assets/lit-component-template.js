import { LitElement, html, css, nothing } from 'lit';

/*
 * Заготовка веб-компонента для Vaadin/Jmix.
 *
 * Положи в src/main/frontend/components/<tag>.js и создай Java-обёртку:
 *
 *   @Tag("app-widget")
 *   @JsModule("./components/app-widget.js")
 *   public class Widget extends Component implements HasSize, HasStyle {
 *       public void setData(String json) {
 *           getElement().setProperty("data", json == null ? "" : json);
 *       }
 *   }
 *
 * Данные приходят строкой JSON в свойство data. Разбор и подготовка выполняются
 * в willUpdate, отрисовка - в render. Цвета берутся из переменных темы, поэтому
 * компонент сам следует светлой и тёмной схеме.
 */
class AppWidget extends LitElement {
  static properties = {
    data: { type: String },
    _selected: { state: true },
    _width: { state: true },
  };

  constructor() {
    super();
    this.data = '';
    this._selected = null;
    this._width = 0;
    this._model = null;
    this._error = null;
  }

  connectedCallback() {
    super.connectedCallback();
    // Ширина нужна для масштаба «по ширине».
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
    if (!changed.has('data')) return;
    this._selected = null;
    this._error = null;
    this._model = null;
    if (!this.data) return;
    try {
      this._model = this._prepare(JSON.parse(this.data));
    } catch (e) {
      this._error = 'Не удалось разобрать данные.';
    }
  }

  /** Превращает входные данные в модель для отрисовки (узлы, позиции и т. п.). */
  _prepare(input) {
    return { items: Array.isArray(input.items) ? input.items : [] };
  }

  render() {
    if (this._error) return html`<div class="message">${this._error}</div>`;
    if (!this._model) return nothing;
    return html`
      <div class="viewport">
        ${this._model.items.map((item) => html`
          <div class="item ${this._selected === item ? 'selected' : ''}"
               @click=${() => { this._selected = this._selected === item ? null : item; }}>
            ${item.title ?? item.id}
          </div>`)}
      </div>
      ${this._selected ? html`
        <div class="inspector">
          <pre>${JSON.stringify(this._selected, null, 2)}</pre>
        </div>` : nothing}
    `;
  }

  static styles = css`
    :host {
      display: flex;
      flex-direction: column;
      position: relative;
      min-height: 0;
      font-family: var(--lumo-font-family);
      color: var(--app-text, #16162a);
    }

    .message {
      padding: 1rem;
      color: var(--lumo-error-text-color, #dc2626);
    }

    .viewport {
      flex: 1;
      min-height: 0;
      overflow: auto;
      padding: 1rem;
      border-radius: 14px;
      border: 1px solid var(--app-border, rgba(0, 0, 0, .1));
      background-color: var(--app-surface-2, #f6f6fb);
      background-image: var(--app-dot-grid);
      background-size: 18px 18px;
    }

    .item {
      padding: .6rem .8rem;
      margin-bottom: .5rem;
      border-radius: 12px;
      background: var(--app-surface, #fff);
      border: 1px solid var(--app-border-strong, rgba(0, 0, 0, .14));
      box-shadow: var(--app-shadow-s);
      cursor: pointer;
      animation: pop .3s ease both;
    }

    .item.selected {
      border-color: var(--lumo-primary-color, #7c5cf5);
    }

    .inspector {
      position: absolute;
      left: 12px;
      right: 12px;
      bottom: 12px;
      max-height: 45%;
      overflow: auto;
      border-radius: 14px;
      background: var(--app-surface, #fff);
      box-shadow: var(--app-shadow-l);
    }

    pre {
      margin: 0;
      padding: .8rem 1rem;
      font: .76rem/1.55 ui-monospace, SFMono-Regular, Menlo, Consolas, monospace;
    }

    @keyframes pop {
      from { opacity: 0; transform: translateY(6px); }
      to { opacity: 1; transform: none; }
    }
  `;
}

customElements.define('app-widget', AppWidget);
