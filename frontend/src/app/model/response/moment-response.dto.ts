import {TagEntity} from "../entities/tag.entity";
import {Type} from "../enum/Type";

export interface MomentResponseDto {
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
  tags: TagEntity[];
  createdAt: string;
  updatedAt: string;
}
