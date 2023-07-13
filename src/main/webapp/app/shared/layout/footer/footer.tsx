import { Row, Col, Typography } from 'antd';
import './footer.scss';

import React from 'react';

const { Text } = Typography;

const Footer = () => (
  <div className="footer page-content">
    <div>
      <Row justify="space-between">
        <Col span={4}>
          <Text type="secondary">Правовая инфрмация</Text>
        </Col>
        <Col span={4}>
          <Text type="secondary">О нас</Text>
        </Col>
      </Row>
      <Row justify="space-between">
        <Col span={4}>
          <Text type="secondary">Партнерам</Text>
        </Col>
        <Col span={4}>
          <Text type="secondary">Пользователям</Text>
        </Col>
      </Row>
    </div>
  </div>
);

export default Footer;
