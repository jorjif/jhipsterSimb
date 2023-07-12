import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ViewsToDate from './views-to-date';
import ViewsToDateDetail from './views-to-date-detail';
import ViewsToDateUpdate from './views-to-date-update';
import ViewsToDateDeleteDialog from './views-to-date-delete-dialog';

const ViewsToDateRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ViewsToDate />} />
    <Route path="new" element={<ViewsToDateUpdate />} />
    <Route path=":id">
      <Route index element={<ViewsToDateDetail />} />
      <Route path="edit" element={<ViewsToDateUpdate />} />
      <Route path="delete" element={<ViewsToDateDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ViewsToDateRoutes;
