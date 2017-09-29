import { TabMapperPage } from './app.po';

describe('tab-mapper App', () => {
  let page: TabMapperPage;

  beforeEach(() => {
    page = new TabMapperPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!');
  });
});
