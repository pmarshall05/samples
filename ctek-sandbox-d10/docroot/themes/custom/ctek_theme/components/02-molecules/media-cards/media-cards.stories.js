import mediacards from './media-cards.twig';

import mediacardsData from './media-cards.yml';
import mediacardsServiceData from './media-cards-service.yml';
import mediacardsArticleData from './media-cards-article.yml';


/**
 * Storybook Definition.
 */
export default { title: 'Molecules/Media Cards' };

export const mediaCards = () => mediacards(mediacardsData);

export const mediaCardsService = () => mediacards(mediacardsServiceData);

export const mediaCardsArticle = () => mediacards(mediacardsArticleData);