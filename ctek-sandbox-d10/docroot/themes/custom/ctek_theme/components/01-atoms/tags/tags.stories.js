// Tag Stories
import tags from './tags.twig';

import tagData from './tags.yml';




/**
 * Storybook Definition.
 */
export default { title: 'Atoms/Tags' };

export const tagList = () => tags(tagData);