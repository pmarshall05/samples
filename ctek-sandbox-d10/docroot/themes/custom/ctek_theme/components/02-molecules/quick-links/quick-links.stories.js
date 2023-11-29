import quicklinks from './quick-links.twig';

import quicklinksData from './quick-links.yml';
import quicklinksBgData from './quick-links-bg.yml';


/**
 * Storybook Definition.
 */
export default { title: 'Molecules/Quick Links' };

export const quickLinks = () => quicklinks(quicklinksData);

export const quickLinksBackground = () => quicklinks(quicklinksBgData);