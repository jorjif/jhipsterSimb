import dayjs from 'dayjs';

export interface IViewPerDate {
  id?: number;
  date?: string;
  views?: number;
}

export const defaultValue: Readonly<IViewPerDate> = {};
