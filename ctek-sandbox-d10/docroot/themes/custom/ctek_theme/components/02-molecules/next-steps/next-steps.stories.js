import nextsteps from './next-steps.twig';

import nextstepsAccentData from './next-steps-accent.yml';
import nextstepsSecondaryData from './next-steps-secondary.yml';


/**
 * Storybook Definition.
 */
export default { title: 'Molecules/Next Steps' };

export const secondary= () => nextsteps(nextstepsSecondaryData);

export const accent= () => nextsteps(nextstepsAccentData);

