import textcards from './text-cards.twig';

import textcardsData from './text-cards.yml';


/**
 * Storybook Definition.
 */
export default { title: 'Molecules/Text Cards' };

export const textCards= () => textcards(textcardsData);