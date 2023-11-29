import link from './link.twig';

import linkData from './link.yml';
import linkXsmallData from './link-xsmall.yml';
import linkSmallData from './link-small.yml';
import linkLargeData from './link-large.yml';

import arrowLinkData from './arrow-link.yml';
import arrowLinkHideIconData from './arrow-link-hide-icon.yml';
import arrowLinkXsmallData from './arrow-link-xsmall.yml';
import arrowLinkSmallData from './arrow-link-small.yml';
import arrowLinkLargeData from './arrow-link-large.yml';

import arrowExternalLinkData from './arrow-external-link.yml';
import arrowExternalLinkXsmallData from './arrow-external-link-xsmall.yml';
import arrowExternalLinkSmallData from './arrow-external-link-small.yml';
import arrowExternalLinkLargeData from './arrow-external-link-large.yml';

import menuLinkData from './menu-link.yml';
import menuLinkXsmallData from './menu-link-xsmall.yml';
import menuLinkSmallData from './menu-link-small.yml';
import menuLinkLargeData from './menu-link-large.yml';

import iconLinkData from './icon-link.yml';
import iconLinkXsmallData from './icon-link-xsmall.yml';
import iconLinkSmallData from './icon-link-small.yml';
import iconLinkLargeData from './icon-link-large.yml';

import iconRightLinkData from './icon-right-link.yml';
import iconRightLinkXsmallData from './icon-right-link-xsmall.yml';
import iconRightLinkSmallData from './icon-right-link-small.yml';
import iconRightLinkLargeData from './icon-right-link-large.yml';


/**
 * Storybook Definition.
 */
export default { title: 'Atoms/Links' };

// Inline Link
export const inlineLink = () => link(linkData);
export const inlineXsmallLink = () => link(linkXsmallData);
export const inlineSmallLink = () => link(linkSmallData);
export const inlineLargeLink = () => link(linkLargeData);


export const arrowLink = () => link(arrowLinkData);
export const arrowLinkHideIcon = () => link(arrowLinkHideIconData);
export const arrowXsmallLink = () => link(arrowLinkXsmallData);
export const arrowSmallLink = () => link(arrowLinkSmallData);
export const arrowLargeLink = () => link(arrowLinkLargeData);

export const arrowExternalLink = () => link(arrowExternalLinkData);
export const arrowExternalXsmallLink = () => link(arrowExternalLinkXsmallData);
export const arrowExternalSmallLink = () => link(arrowExternalLinkSmallData);
export const arrowExternalLargeLink = () => link(arrowExternalLinkLargeData);

export const mainMenuLink = () => link(menuLinkData);
export const mainMenuXsmallLink = () => link(menuLinkXsmallData);
export const mainMenuSmallLink = () => link(menuLinkSmallData);
export const mainMenuLargeLink = () => link(menuLinkLargeData);

export const iconLink = () => link(iconLinkData);
export const iconXsmallLink = () => link(iconLinkXsmallData);
export const iconSmallLink = () => link(iconLinkSmallData);
export const iconLargeLink = () => link(iconLinkLargeData);

export const iconRightLink = () => link(iconRightLinkData);
export const iconRightXsmallLink = () => link(iconRightLinkXsmallData);
export const iconRightSmallLink = () => link(iconRightLinkSmallData);
export const iconRightLargeLink = () => link(iconRightLinkLargeData);