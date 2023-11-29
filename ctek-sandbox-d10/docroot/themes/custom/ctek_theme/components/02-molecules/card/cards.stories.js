import card from './card.twig';

import cardData from './card.yml';
import cardBgData from './card-bg.yml';
import cardTextData from './card-text.yml';

/**
 * Storybook Definition.
 */
export default { title: 'Molecules/Cards' };

export const cardExample = () => card(cardData);

export const text = () => card(cardTextData);

export const textWithBackground = () => card({
	...cardTextData,
	...cardBgData});

export const cardWithBackground = () => card({ ...cardData, ...cardBgData });
