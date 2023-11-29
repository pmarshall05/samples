import siteFooter from './site-footer/site-footer.twig';
import siteHeader from './site-header/site-header.twig';

import footerSocial from '../../02-molecules/menus/social-menu/social-menu.yml';
import footerMenu from '../../02-molecules/menus/footer/footer.yml';
import breadcrumbData from '../../02-molecules/menus/breadcrumbs/breadcrumbs.yml';
import mainMenuData from '../../02-molecules/menus/main-menu/main-menu.yml';
import utilityMenuData from '../../02-molecules/menus/utility-menu/utility-menu.yml';
import usertypeData from '../../02-molecules/selector/user-type/user-type.yml';
import languageData from '../../02-molecules/selector/language/language.yml';
import logoData from '../../02-molecules/logo/logo.yml';

import '../../02-molecules/menus/main-menu/main-menu';
import '../../02-molecules/menus/utility-menu/utility-menu';
import '../../02-molecules/selector/user-type/user-type';
import '../../02-molecules/selector/language/language';

/**
 * Storybook Definition.
 */
export default {
  title: 'Organisms/Site',
  parameters: {
    layout: 'fullscreen',
  },
};

export const footer = () => siteFooter({ ...footerSocial, ...footerMenu });

export const header = () =>
  siteHeader({
    ...breadcrumbData,
    ...mainMenuData,
    ...utilityMenuData,
    ...usertypeData,
    ...languageData,
    ...logoData,
  });
