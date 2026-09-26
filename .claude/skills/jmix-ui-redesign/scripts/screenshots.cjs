#!/usr/bin/env node
/*
 * Скриншоты Jmix-приложения через Playwright.
 *
 * Шаги сценария передаются JSON-массивом первым аргументом. Поддерживаемые шаги:
 *   {"goto": "/login"}                   открыть путь относительно BASE_URL
 *   {"login": true}                      войти (USERNAME / PASSWORD из окружения)
 *   {"click": "<селектор Playwright>"}
 *   {"fill": ["<селектор>", "<текст>"]}
 *   {"press": "Enter"}                   нажать клавишу или сочетание
 *   {"wait": 1500}                       подождать, мс
 *   {"eval": "<выражение JS>"}           вычислить в странице и вывести результат
 *   {"shot": "имя"}                      сохранить OUT/имя.png
 * В одном шаге можно совмещать несколько действий, они выполняются в порядке выше.
 *
 * Окружение: BASE_URL (http://localhost:8080), USERNAME/PASSWORD (admin/admin),
 * OUT (.), W и H (1440x900), SCHEME (light|dark).
 *
 * Пример:
 *   SCHEME=dark node screenshots.cjs '[{"goto":"/login","wait":800,"shot":"login"},
 *     {"login":true,"wait":1500,"shot":"main"}]'
 *
 * Расширение .cjs нужно потому, что package.json Vaadin-проектов объявляет
 * "type": "module", и обычный .js был бы ES-модулем без require.
 *
 * Playwright берётся из локальных node_modules, а если его там нет - из глобальной
 * установки (npm root -g).
 */
const path = require('path');

function loadPlaywright() {
  try {
    return require('playwright');
  } catch (e) {
    const globalRoot = require('child_process').execSync('npm root -g').toString().trim();
    return require(path.join(globalRoot, 'playwright'));
  }
}

const { chromium } = loadPlaywright();
const base = process.env.BASE_URL || 'http://localhost:8080';
const out = process.env.OUT || '.';
const steps = JSON.parse(process.argv[2] || '[]');

(async () => {
  const browser = await chromium.launch();
  const context = await browser.newContext({
    viewport: { width: +(process.env.W || 1440), height: +(process.env.H || 900) },
    colorScheme: process.env.SCHEME || 'light',
  });
  const page = await context.newPage();
  page.on('pageerror', (e) => console.log('PAGEERROR:', e.message));
  page.on('console', (m) => { if (m.type() === 'error') console.log('CONSOLE:', m.text()); });

  for (const s of steps) {
    if (s.goto) await page.goto(base + s.goto, { waitUntil: 'networkidle', timeout: 120000 });
    if (s.login) {
      await page.fill('input[name=username]', process.env.USERNAME || 'admin');
      await page.fill('input[name=password]', process.env.PASSWORD || 'admin');
      // Enter в поле пароля отправляет форму при любой разметке кнопки.
      await page.press('input[name=password]', 'Enter');
      await page.waitForTimeout(2500);
    }
    if (s.click) await page.click(s.click, { timeout: 15000 });
    if (s.fill) await page.fill(s.fill[0], s.fill[1]);
    if (s.press) await page.keyboard.press(s.press);
    if (s.eval) console.log('EVAL:', JSON.stringify(await page.evaluate(s.eval)));
    if (s.wait) await page.waitForTimeout(s.wait);
    if (s.shot) await page.screenshot({ path: path.join(out, `${s.shot}.png`) });
  }
  await browser.close();
})().catch((e) => {
  console.error(e);
  process.exit(1);
});
