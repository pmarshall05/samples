import heroctas from './hero-ctas.twig';

import rowIconData from './hero-ctas-row.yml';
import columnIconData from './hero-ctas-column.yml';



/**
 * Storybook Definition.
 */
export default { title: 'Molecules/Hero CTAs' };

export const row = () => heroctas(rowIconData);

export const column = () => heroctas(columnIconData);