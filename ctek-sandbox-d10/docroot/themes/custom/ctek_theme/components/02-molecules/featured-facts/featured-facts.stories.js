import featuredfacts from './featured-facts.twig';

import featuredfactsAccentData from './featured-facts-accent.yml';
import featuredfactsPrimaryData from './featured-facts-primary.yml';
import featuredfactsSecondaryData from './featured-facts-secondary.yml';


/**
 * Storybook Definition.
 */
export default { title: 'Molecules/Featured Facts' };

export const primrary= () => featuredfacts(featuredfactsPrimaryData);

export const secondary= () => featuredfacts(featuredfactsSecondaryData);

export const accent= () => featuredfacts(featuredfactsAccentData);