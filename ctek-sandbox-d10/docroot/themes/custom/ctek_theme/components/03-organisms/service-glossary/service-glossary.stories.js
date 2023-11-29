import glossary from './service-glossary.twig';

import glossaryData from './service-glossary.yml';

import '../../02-molecules/tabs/tabs';

/**
 * Storybook Definition.
 */
export default { title: 'Organisms/Service Glossary' };

export const serviceGlossary = () => glossary(glossaryData);
