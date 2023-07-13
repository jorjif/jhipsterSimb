import React, { useEffect, useMemo } from 'react';
import { Line } from '@nivo/line';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import './stats.scss';
import { getEntities, reset } from 'app/entities/posts/posts.reducer';
import { AutoSizer } from 'react-virtualized';
import day from 'dayjs';

interface ChartData {
  id: string | number;
  data: {
    x: number | string | Date;
    y: number | string | Date;
  }[];
}

const baseData: ChartData = {
  id: 0,
  data: [{ x: 0, y: 0 }],
};

export const Chart = () => {
  const dispatch = useAppDispatch();

  const postsList = useAppSelector(state => state.posts.entities);
  const loading = useAppSelector(state => state.posts.loading);
  const updateSuccess = useAppSelector(state => state.posts.updateSuccess);
  const getAllEntities = () => {
    dispatch(getEntities({}));
  };

  const resetAll = () => {
    dispatch(reset());
    dispatch(getEntities({}));
  };

  const chartData = useMemo(() => {
    const minifiedPostsList: ChartData[] = postsList?.map(({ views, date }) => {
      return { x: new Date(date), y: views };
    });
    return { data: minifiedPostsList, id: 'views' };
  }, [postsList]);

  useEffect(() => {
    resetAll();
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      resetAll();
    }
  }, [updateSuccess]);

  useEffect(() => {
    getAllEntities();
  }, []);

  return (
    <div className="chart">
      {chartData ? (
        <AutoSizer>
          {({ height, width }) => {
            return (
              <Line
                height={height}
                width={width}
                data={[chartData] || []}
                colors={{ scheme: 'pastel1' }}
                // xFormat={(date: Date) => day(date).format('DD.MM.YYYY')}
                margin={{ top: 20, right: 20, bottom: 50, left: 80 }}
                xScale={{ type: 'point' }}
                yScale={{
                  type: 'linear',
                  min: 'auto',
                  max: 'auto',
                  stacked: true,
                  reverse: false,
                }}
                axisBottom={{
                  tickSize: 5,
                  tickPadding: 5,
                  tickRotation: 0,
                  legend: 'Даты',
                  legendOffset: 40,
                  legendPosition: 'middle',
                  format: (date: Date) => day(date).format('DD.MM.YYYY'),
                }}
                axisLeft={{
                  tickSize: 5,
                  tickPadding: 5,
                  tickRotation: 0,
                  legend: 'Просмотры',
                  legendOffset: -60,
                  legendPosition: 'middle',
                }}
              />
            );
          }}
        </AutoSizer>
      ) : null}
    </div>
  );
};
