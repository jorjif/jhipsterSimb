import React from 'react';
import { Translate } from 'react-jhipster';

import { NavItem, NavLink, NavbarBrand } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const BrandIcon = props => (
  <div {...props} className="brand-icon">
    <img src="content/images/lightning.png" alt="Logo" />
  </div>
);

export const Brand = () => (
  <NavbarBrand tag={Link} to="/" className="brand-logo">
    <BrandIcon />
    <span className="brand-title">
      <Translate contentKey="global.title">Jhipster</Translate>
    </span>
    <span className="navbar-version">{VERSION}</span>
  </NavbarBrand>
);

export const Home = () => (
  <NavItem>
    <NavLink tag={Link} to="/" className="d-flex align-items-center">
      <FontAwesomeIcon icon="home" />
      <span>
        <Translate contentKey="global.menu.home">Home</Translate>
      </span>
    </NavLink>
  </NavItem>
);

export const Posts = () => (
  <NavItem>
    <NavLink tag={Link} to="/postsList" className="d-flex align-items-center">
      <span>
        <Translate contentKey="global.menu.posts">Посты</Translate>
      </span>
    </NavLink>
  </NavItem>
);

export const Chart = () => (
  <NavItem>
    <NavLink tag={Link} to="/chart" className="d-flex align-items-center">
      <span>
        <Translate contentKey="global.menu.chart">Чарты</Translate>
      </span>
    </NavLink>
  </NavItem>
);
