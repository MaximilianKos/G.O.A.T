import {Tag} from "../entities/Tag";
import {Type} from "../enum/Type";

export interface MomentResponse {
  id: number;
  title: string;
  sourceUrl: string;
  description: string;
  type: Type;
  thumbnailUrl: string;
  localPath: string;
  archivedAt: string;
  clicks: number;
  archived: boolean;
  tags: Tag[];
  createdAt: string;
  updatedAt: string;
}
