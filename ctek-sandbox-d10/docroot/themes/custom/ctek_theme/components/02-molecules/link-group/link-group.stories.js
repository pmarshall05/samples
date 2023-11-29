import linkgroup from './link-group.twig';

import rowData from './link-group-row.yml';
import columnData from './link-group-column.yml';

import rowArrowData from './link-group-row-arrow.yml';
import columnArrowData from './link-group-column-arrow.yml';

import rowIconData from './link-group-row-icon.yml';
import columnIconData from './link-group-column-icon.yml';



/**
 * Storybook Definition.
 */
export default { title: 'Molecules/Link Group' };

export const row = () => linkgroup(rowData);

export const column = () => linkgroup(columnData);

export const rowArrow = () => linkgroup(rowArrowData);

export const columnArrow = () => linkgroup(columnArrowData);

export const rowIcon = () => linkgroup(rowIconData);

export const columnIcon = () => linkgroup(columnIconData);