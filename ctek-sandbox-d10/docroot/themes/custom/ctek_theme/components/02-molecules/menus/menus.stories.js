import breadcrumb from './breadcrumbs/breadcrumbs.twig';
import inlineMenu from './inline/inline-menu.twig';
import mainMenu from './main-menu/main-menu.twig';
import socialMenu from './social-menu/social-menu.twig';
import utilityMenu from './utility-menu/utility-menu.twig';
import footerMenu from './footer/footer.twig';

import breadcrumbsData from './breadcrumbs/breadcrumbs.yml';
import inlineMenuData from './inline/inline-menu.yml';
import mainMenuData from './main-menu/main-menu.yml';
import socialMenuData from './social-menu/social-menu.yml';
import utilityMenuData from './utility-menu/utility-menu.yml';
import footerData from './footer/footer.yml';

import usertypeData from '../selector/user-type/user-type.yml';
import languageData from '../selector/language/language.yml';

import './main-menu/main-menu';
import './utility-menu/utility-menu';
import '../selector/user-type/user-type';
import '../selector/language/language';

import '../../../images/doctors.jpeg';
import '../../../images/bill-pay.jpg';
import '../../../images/hospital-1.jpg';

/**
 * Storybook Definition.
 */
export default { title: 'Molecules/Menus' };

export const breadcrumbs = () => breadcrumb(breadcrumbsData);

export const inline = () => inlineMenu(inlineMenuData);

export const main = () => mainMenu({
    ...mainMenuData,
    ...usertypeData,
    ...languageData,
  });

export const social = () => socialMenu(socialMenuData);

export const utility = () => utilityMenu(utilityMenuData);

export const footer = () => footerMenu({
    ...footerData,
    ...socialMenuData,
  });
