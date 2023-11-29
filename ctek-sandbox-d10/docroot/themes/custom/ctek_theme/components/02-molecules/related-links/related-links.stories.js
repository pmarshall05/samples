import relatedlinks from './related-links.twig';

import relatedLinksData from './related-links.yml';

import './related-links';


/**
 * Storybook Definition.
 */
export default { title: 'Molecules/Related Links' };

export const links= () => relatedlinks(relatedLinksData);