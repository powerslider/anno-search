import { Pipe, PipeTransform } from '@angular/core';
import { AnnoDocument } from './model/annodocument';
import { AnnotationInfo } from './model/annotation';

@Pipe({
  name: 'colorParagraph'
})
export class ColorParagraphPipe implements PipeTransform {

  transform(value: AnnoDocument, args?: any): string {
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
      result.push(this.wrap(p, anno.string + " : " + anno.type));
      lastPushed = anno.end - 1;
    }
    if (lastPushed + 1 < value.text.length) {
      result.push(value.text.substring(lastPushed + 1));
    }
    return result.join("").replace(/(?:\r\n|\r|\n)/g, '<br />');
  }

  private wrap(value: string, title?: string): string {
    title = title != null ? "title=\"" + title + "\"" : "";
    return "<span class=\"anno1\" " + title + ">" + value + "</span>";
  }
}
