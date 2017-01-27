import { Pipe, PipeTransform } from '@angular/core';
import { AnnoDocument } from './model/annodocument';
import { AnnotationInfo } from './model/annotation';

import { BrowserModule, DomSanitizer, SafeHtml } from '@angular/platform-browser'

@Pipe({
  name: 'colorParagraph'
})
export class ColorParagraphPipe implements PipeTransform {

  constructor(private _sanitizer: DomSanitizer) { }


  transform(value: AnnoDocument, args?: any): SafeHtml {
    if (value.annotations == null || value.annotations.length == 0) {
      return "";
    }

    let result: string[] = [];

    let annos: AnnotationInfo[] = value.annotations.sort((a, b) => {
      return a.start - b.start;
    });
    let lastPushed: number = 0;
    if (annos[0].start > 0) {
      result.push(value.text.substring(0, annos[0].start));
      lastPushed = annos[0].start;
    }
    for (let anno of value.annotations) {
      if (lastPushed + 1 < anno.start) {
        result.push(value.text.substring(lastPushed + 1, anno.start));
        lastPushed = anno.start;
      }
      let p: string = value.text.substring(anno.start, anno.end);
      result.push(this.wrap(p, anno));
      lastPushed = anno.end - 1;
    }
    if (lastPushed + 1 < value.text.length) {
      result.push(value.text.substring(lastPushed + 1));
    }
    return this._sanitizer.bypassSecurityTrustHtml(result.join("").replace(/(?:\r\n|\r|\n)/g, '<br />'));
  }

  private colors: number = 5;
  private usedColors: number = 0;

  private wrap(value: string, anno: AnnotationInfo): string {
    let colorCode: number = (this.usedColors++ % this.colors) + 1;
    let clas: string = "anno" + colorCode;
    return "<span class=\"anno " + clas + " tooltip\">" + this.gen(anno.string, anno.type, colorCode) + value + "</span>";
  }

  private gen(s: string, type: string, colorCode: number): string {
    return "<div class=\"tooltiptext tooltip-color" + colorCode + "\">" +
      "<span>Keyword: " + s + "</span><br/>" +
      "<span>Type: " + type + "</span>" +
      "</div>";
  }
}
