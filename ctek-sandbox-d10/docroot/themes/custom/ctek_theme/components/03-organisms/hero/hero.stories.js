// Hero Stories
import homepage from './homepage/homepage.twig';
import article from './article/article.twig';
import service from './service/service.twig';

import homepageData from './homepage/homepage.yml';
import articleData from './article/article.yml';
import serviceData from './service/service.yml';

/**
 * Storybook Definition.
 */
export default { title: 'organisms/Hero' };

export const homepageHero = () => homepage(homepageData);

export const articleHero = () => article(articleData);

export const serviceHero = () => service(serviceData);