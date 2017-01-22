import { AnnoSearchUiPage } from './app.po';

describe('anno-search-ui App', function() {
  let page: AnnoSearchUiPage;

  beforeEach(() => {
    page = new AnnoSearchUiPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
