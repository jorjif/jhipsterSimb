import dayjs from 'dayjs';

export interface IPosts {
  id?: number;
  title?: string;
  content?: string;
  date?: string;
  views?: number;
  imgContentType?: string | null;
  img?: string | null;
}

export const defaultValue: Readonly<IPosts> = {};
