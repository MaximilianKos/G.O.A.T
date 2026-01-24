import {Type} from "../enum/Type";

export interface CreateMomentRequestDto {
  title: string;
  sourceUrl: string;
  description?: string;
  type: Type;
  thumbnailUrl: string;

  tagIds: number[];
}
