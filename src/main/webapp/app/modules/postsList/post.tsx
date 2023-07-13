import React from 'react';
import { Row, Col, Typography, Card } from 'antd';
import { TextFormat, byteSize } from 'react-jhipster';
import { APP_DATE_FORMAT } from 'app/config/constants';
const { Text, Title } = Typography;
interface IProps {
  title: string;
  content: string;
  date: string;
  img: string;
  views: number;
  imgContentType: string;
}

export const Post = ({ title, content, date, img, views, imgContentType }: IProps) => {
  return (
    <Card>
      <Row align="top">
        <Col span={6}>{img ? <div>{imgContentType ? <img src={`data:${imgContentType};base64,${img}`} /> : null}</div> : null}</Col>
        <Col span={18}>
          <Row justify="start">
            <Col span={24}>
              <Title>{title}</Title>
            </Col>
          </Row>
          <Row justify="start">
            <Col span={24}>
              <Text>{content}</Text>
            </Col>
          </Row>
        </Col>
      </Row>
      <Row justify="space-between">
        <Col span={12}>
          <Text type="secondary">
            <TextFormat type="date" value={date} format={APP_DATE_FORMAT} />
          </Text>
        </Col>
        <Col span={12}>
          <Text type="secondary">просмотры: {views}</Text>
        </Col>
      </Row>
    </Card>
  );
};
