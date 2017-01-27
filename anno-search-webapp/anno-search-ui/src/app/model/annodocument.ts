import { AnnotationInfo } from './annotation';

export class AnnoDocument {
    public id: number;
    public text: string;
    public annotations: Array<AnnotationInfo> = [];
    public title: string;
    constructor() {

    }
}