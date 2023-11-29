import usertype from './user-type/user-type.twig';
import language from './language/language.twig';

import usertypeData from './user-type/user-type.yml';
import languageData from './language/language.yml';

import './user-type/user-type';
import './language/language';


/**
 * Storybook Definition.
 */
export default { title: 'Molecules/Selectors' };

export const usertypeSelector = () => usertype(usertypeData);

export const languageSelector = () => language(languageData);