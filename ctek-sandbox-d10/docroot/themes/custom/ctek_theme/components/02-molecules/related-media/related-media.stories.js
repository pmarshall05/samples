import relatedmedia from './related-media.twig';

import relatedmediaData from './related-media.yml';


import './related-media';


/**
 * Storybook Definition.
 */
export default { title: 'Molecules/Related Media' };

export const relatedMedia = () => relatedmedia(relatedmediaData);