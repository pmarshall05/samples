// Buttons Stories
import button from './button.twig';

//Primary
import buttonData from './button.yml';
import buttonXsmallData from './button-xsmall.yml';
import buttonSmallData from './button-small.yml';
import buttonLargeData from './button-large.yml';
import buttonDisabledData from './button-disabled.yml';
import buttonIconFrontData from './button-icon-front.yml';
import buttonIconEndData from './button-icon-end.yml';

//Secondary
import buttonSecondaryData from './button-secondary.yml';
import buttonSecondaryXsmallData from './button-secondary-xsmall.yml';
import buttonSecondarySmallData from './button-secondary-small.yml';
import buttonSecondaryLargeData from './button-secondary-large.yml';
import buttonSecondaryDisabledData from './button-secondary-disabled.yml';
import buttonSecondaryIconFrontData from './button-secondary-icon-front.yml';
import buttonSecondaryIconEndData from './button-secondary-icon-end.yml';

//Tertiary
import buttonTertiaryData from './button-tertiary.yml';
import buttonTertiaryXsmallData from './button-tertiary-xsmall.yml';
import buttonTertiarySmallData from './button-tertiary-small.yml';
import buttonTertiaryLargeData from './button-tertiary-large.yml';
import buttonTertiaryDisabledData from './button-tertiary-disabled.yml';
import buttonTertiaryIconFrontData from './button-tertiary-icon-front.yml';
import buttonTertiaryIconEndData from './button-tertiary-icon-end.yml';

//Outline
import buttonOutlineData from './button-outline.yml';
import buttonOutlineXsmallData from './button-outline-xsmall.yml';
import buttonOutlineSmallData from './button-outline-small.yml';
import buttonOutlineLargeData from './button-outline-large.yml';
import buttonOutlineDisabledData from './button-outline-disabled.yml';
import buttonOutlineIconFrontData from './button-outline-icon-front.yml';
import buttonOutlineIconEndData from './button-outline-icon-end.yml';

/**
 * Storybook Definition.
 */
export default { title: 'Atoms/Button' };

export const primary = () => button(buttonData);
export const primary_xsmall = () => button(buttonXsmallData);
export const primary_small = () => button(buttonSmallData);
export const primary_large = () => button(buttonLargeData);
export const primary_disabled = () => button(buttonDisabledData);
export const primary_icon_front = () => button(buttonIconFrontData);
export const primary_icon_end = () => button(buttonIconEndData);

export const secondary = () => button(buttonSecondaryData);
export const secondary_xsmall = () => button(buttonSecondaryXsmallData);
export const secondary_small = () => button(buttonSecondarySmallData);
export const secondary_large = () => button(buttonSecondaryLargeData);
export const secondary_disabled = () => button(buttonSecondaryDisabledData);
export const secondary_icon_front = () => button(buttonSecondaryIconFrontData);
export const secondary_icon_end = () => button(buttonSecondaryIconEndData);

export const tertiary = () => button(buttonTertiaryData);
export const tertiary_xsmall = () => button(buttonTertiaryXsmallData);
export const tertiary_small = () => button(buttonTertiarySmallData);
export const tertiary_large = () => button(buttonTertiaryLargeData);
export const tertiary_disabled = () => button(buttonTertiaryDisabledData);
export const tertiary_icon_front = () => button(buttonTertiaryIconFrontData);
export const tertiary_icon_end = () => button(buttonTertiaryIconEndData);

export const outline = () => button(buttonOutlineData);
export const outline_xsmall = () => button(buttonOutlineXsmallData);
export const outline_small = () => button(buttonOutlineSmallData);
export const outline_large = () => button(buttonOutlineLargeData);
export const outline_disabled = () => button(buttonOutlineDisabledData);
export const outline_icon_front = () => button(buttonOutlineIconFrontData);
export const outline_icon_end = () => button(buttonOutlineIconEndData);


