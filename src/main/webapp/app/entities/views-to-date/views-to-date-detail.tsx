import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './views-to-date.reducer';

export const ViewsToDateDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const viewsToDateEntity = useAppSelector(state => state.viewsToDate.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="viewsToDateDetailsHeading">
          <Translate contentKey="jhipsterApp.viewsToDate.detail.title">ViewsToDate</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{viewsToDateEntity.id}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="jhipsterApp.viewsToDate.date">Date</Translate>
            </span>
          </dt>
          <dd>{viewsToDateEntity.date}</dd>
          <dt>
            <span id="views">
              <Translate contentKey="jhipsterApp.viewsToDate.views">Views</Translate>
            </span>
          </dt>
          <dd>{viewsToDateEntity.views}</dd>
        </dl>
        <Button tag={Link} to="/views-to-date" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/views-to-date/${viewsToDateEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ViewsToDateDetail;
