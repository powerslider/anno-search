/* tslint:disable:no-unused-variable */

import { TestBed, async } from '@angular/core/testing';
import { ColorParagraphPipe } from './color-paragraph.pipe';

import { AnnoDocument } from './model/annodocument';
import { AnnotationInfo } from './model/annotation';

describe('ColorParagraphPipe', () => {
  it('create an instance', () => {
    const pipe = new ColorParagraphPipe();
    expect(pipe).toBeTruthy();
  });
  it('should work', () => {
    const pipe = new ColorParagraphPipe();
    let d = new AnnoDocument();
    d.text = "This is a text. And this is another text.";
    d.annotations = [{ type: "ivan", start: 5, end: 6 },
    { type: "peter", start: 8, end: 8 }];
    console.log(pipe.transform(d));
  });
});
