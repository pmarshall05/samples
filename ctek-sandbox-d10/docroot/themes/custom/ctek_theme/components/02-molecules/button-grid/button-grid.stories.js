import buttongrid from './button-grid.twig';

import buttongridData from './button-grid.yml';


/**
 * Storybook Definition.
 */
export default { title: 'Molecules/Button Grid' };

export const buttonGrid= () => buttongrid(buttongridData);