import singlecolorcta from './single-color-cta.twig';

import singlecolorctaAccentData from './single-color-cta-accent.yml';
import singlecolorctaPrimaryData from './single-color-cta-primary.yml';
import singlecolorctaSecondaryData from './single-color-cta-secondary.yml';


/**
 * Storybook Definition.
 */
export default { title: 'Molecules/Single Color CTA' };

export const primary= () => singlecolorcta(singlecolorctaPrimaryData);

export const secondary= () => singlecolorcta(singlecolorctaSecondaryData);

export const accent= () => singlecolorcta(singlecolorctaAccentData);

