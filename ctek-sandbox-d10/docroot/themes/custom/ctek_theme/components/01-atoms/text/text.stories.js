import heading from './headings/_heading.twig';
import blockquote from './text/02-blockquote.twig';
import pre from './text/05-pre.twig';
import paragraph from './text/03-inline-elements.twig';
import code from './text/07-code.twig';
import subhead from './text/08-subhead.twig';
import eyebrow from './text/09-eyebrow.twig';

import blockquoteData from './text/blockquote.yml';
import headingData from './headings/headings.yml';
import codeData from './text/code.yml';
import subheadData from './text/subhead.yml';
import eyebrowData from './text/eyebrow.yml';

/**
 * Storybook Definition.
 */
export default { title: 'Atoms/Text' };

// Loop over items in headingData to show each one in the example below.
const headings = headingData.map((d) => heading(d)).join('');

export const headingsExamples = () => headings;

export const blockquoteExample = () => blockquote(blockquoteData);

export const preformatted = () => pre();

export const random = () => paragraph();

export const codeExample = () => code(codeData);

export const subheadExample = () => subhead(subheadData);

export const eyebrowExample = () => eyebrow(eyebrowData);
