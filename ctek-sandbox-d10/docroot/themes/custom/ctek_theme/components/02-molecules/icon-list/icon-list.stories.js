import iconlist from './icon-list.twig';

import iconlistData from './icon-list.yml';


/**
 * Storybook Definition.
 */
export default { title: 'Molecules/Icon List' };

export const list= () => iconlist(iconlistData);