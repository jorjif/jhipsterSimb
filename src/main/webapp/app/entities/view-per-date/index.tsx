import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ViewPerDate from './view-per-date';
import ViewPerDateDetail from './view-per-date-detail';
import ViewPerDateUpdate from './view-per-date-update';
import ViewPerDateDeleteDialog from './view-per-date-delete-dialog';

const ViewPerDateRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ViewPerDate />} />
    <Route path="new" element={<ViewPerDateUpdate />} />
    <Route path=":id">
      <Route index element={<ViewPerDateDetail />} />
      <Route path="edit" element={<ViewPerDateUpdate />} />
      <Route path="delete" element={<ViewPerDateDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ViewPerDateRoutes;
