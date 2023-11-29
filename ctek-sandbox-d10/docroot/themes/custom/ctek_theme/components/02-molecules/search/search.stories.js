import services from './service/services-search.twig';

import servicesResultsHeader from './service/services-search-results-header.twig';

import servicesData from './service/services-search.yml';

import servicesResultsHeaderData from './service/services-search-results-header.yml';

import './service/services-search';

/**
 * Storybook Definition.
 */
export default { title: 'Molecules/Search' };

export const servicesSearch = () => services(servicesData);

export const servicesSearchResultsHeader = () => servicesResultsHeader(servicesResultsHeaderData);

