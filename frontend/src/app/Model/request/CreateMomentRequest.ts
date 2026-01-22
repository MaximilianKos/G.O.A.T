import {Type} from "../enum/Type";

export interface CreateMomentRequest {
  title: string;
  sourceUrl: string;
  description?: string;
  type: Type;
  thumbnailUrl: string;

  tagIds: number[];
}
