import testimonial from './testimonial.twig';

import testimonialData from './testimonial.yml';
import testimonialImageData from './testimonial-image.yml';



/**
 * Storybook Definition.
 */
export default { title: 'Molecules/Testimonial' };

export const testimonialNoImage = () => testimonial(testimonialData);

export const testimonialImage = () => testimonial(testimonialImageData);