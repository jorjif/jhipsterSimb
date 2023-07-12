import dayjs from 'dayjs';
import { IViewsToDate } from 'app/shared/model/views-to-date.model';

export interface IPost {
  id?: number;
  title?: string;
  content?: string;
  date?: string;
  views?: number;
  imgContentType?: string | null;
  img?: string | null;
  post?: IViewsToDate | null;
}

export const defaultValue: Readonly<IPost> = {};
