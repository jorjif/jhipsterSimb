import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './view-per-date.reducer';

export const ViewPerDateDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const viewPerDateEntity = useAppSelector(state => state.viewPerDate.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="viewPerDateDetailsHeading">
          <Translate contentKey="jhipsterApp.viewPerDate.detail.title">ViewPerDate</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{viewPerDateEntity.id}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="jhipsterApp.viewPerDate.date">Date</Translate>
            </span>
          </dt>
          <dd>
            {viewPerDateEntity.date ? <TextFormat value={viewPerDateEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="views">
              <Translate contentKey="jhipsterApp.viewPerDate.views">Views</Translate>
            </span>
          </dt>
          <dd>{viewPerDateEntity.views}</dd>
        </dl>
        <Button tag={Link} to="/view-per-date" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/view-per-date/${viewPerDateEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ViewPerDateDetail;
