import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Posts from './posts';
import PostsDetail from './posts-detail';
import PostsUpdate from './posts-update';
import PostsDeleteDialog from './posts-delete-dialog';

const PostsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Posts />} />
    <Route path="new" element={<PostsUpdate />} />
    <Route path=":id">
      <Route index element={<PostsDetail />} />
      <Route path="edit" element={<PostsUpdate />} />
      <Route path="delete" element={<PostsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PostsRoutes;
