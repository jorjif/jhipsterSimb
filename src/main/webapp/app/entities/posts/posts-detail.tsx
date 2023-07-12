import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './posts.reducer';

export const PostsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const postsEntity = useAppSelector(state => state.posts.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="postsDetailsHeading">
          <Translate contentKey="jhipsterApp.posts.detail.title">Posts</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{postsEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="jhipsterApp.posts.title">Title</Translate>
            </span>
          </dt>
          <dd>{postsEntity.title}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="jhipsterApp.posts.content">Content</Translate>
            </span>
          </dt>
          <dd>{postsEntity.content}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="jhipsterApp.posts.date">Date</Translate>
            </span>
          </dt>
          <dd>{postsEntity.date ? <TextFormat value={postsEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="views">
              <Translate contentKey="jhipsterApp.posts.views">Views</Translate>
            </span>
          </dt>
          <dd>{postsEntity.views}</dd>
          <dt>
            <span id="img">
              <Translate contentKey="jhipsterApp.posts.img">Img</Translate>
            </span>
          </dt>
          <dd>
            {postsEntity.img ? (
              <div>
                {postsEntity.imgContentType ? (
                  <a onClick={openFile(postsEntity.imgContentType, postsEntity.img)}>
                    <img src={`data:${postsEntity.imgContentType};base64,${postsEntity.img}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {postsEntity.imgContentType}, {byteSize(postsEntity.img)}
                </span>
              </div>
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/posts" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/posts/${postsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PostsDetail;
