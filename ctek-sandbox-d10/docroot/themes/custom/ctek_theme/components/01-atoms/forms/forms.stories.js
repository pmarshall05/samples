import checkbox from './checkbox/checkbox.twig';
import radio from './radio/radio.twig';
import select from './select/select.twig';
import textfields from './textfields/textfields.twig';
import input from './textfields/input.twig';

import checkboxData from './checkbox/checkbox.yml';
import radioData from './radio/radio.yml';
import selectOptionsData from './select/select.yml';
import inputData from './textfields/input.yml';

/**
 * Storybook Definition.
 */
export default { title: 'Atoms/Forms' };

export const checkboxes = () => checkbox(checkboxData);

export const radioButtons = () => radio(radioData);

export const selectDropdowns = () => select(selectOptionsData);

export const input_textfield = () => input(inputData);

export const textfieldsExamples = () => textfields();
