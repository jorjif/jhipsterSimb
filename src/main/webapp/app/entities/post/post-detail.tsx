import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './post.reducer';

export const PostDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const postEntity = useAppSelector(state => state.post.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="postDetailsHeading">
          <Translate contentKey="jhipsterApp.post.detail.title">Post</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{postEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="jhipsterApp.post.title">Title</Translate>
            </span>
          </dt>
          <dd>{postEntity.title}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="jhipsterApp.post.content">Content</Translate>
            </span>
          </dt>
          <dd>{postEntity.content}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="jhipsterApp.post.date">Date</Translate>
            </span>
          </dt>
          <dd>{postEntity.date ? <TextFormat value={postEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="views">
              <Translate contentKey="jhipsterApp.post.views">Views</Translate>
            </span>
          </dt>
          <dd>{postEntity.views}</dd>
          <dt>
            <span id="img">
              <Translate contentKey="jhipsterApp.post.img">Img</Translate>
            </span>
          </dt>
          <dd>
            {postEntity.img ? (
              <div>
                {postEntity.imgContentType ? (
                  <a onClick={openFile(postEntity.imgContentType, postEntity.img)}>
                    <img src={`data:${postEntity.imgContentType};base64,${postEntity.img}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {postEntity.imgContentType}, {byteSize(postEntity.img)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="jhipsterApp.post.post">Post</Translate>
          </dt>
          <dd>{postEntity.post ? postEntity.post.views : ''}</dd>
        </dl>
        <Button tag={Link} to="/post" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/post/${postEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PostDetail;
