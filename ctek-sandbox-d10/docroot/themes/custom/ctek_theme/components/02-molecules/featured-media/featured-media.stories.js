import featuredMedia from './featured-media.twig';

import imageData from './featured-media-image.yml';
import videoEmbedData from './featured-media-video-embed.yml';
import videoHtml5Data from './featured-media-video-html5.yml';



/**
 * Storybook Definition.
 */
export default { title: 'Molecules/Featured Media' };

export const imageMedia = () => featuredMedia(imageData);

export const videoEmbedMedia = () => featuredMedia(videoEmbedData);

export const videoHtml5Media = () => featuredMedia(videoHtml5Data);