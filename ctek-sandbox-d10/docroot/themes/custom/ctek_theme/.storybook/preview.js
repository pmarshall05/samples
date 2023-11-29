//import { addDecorator } from '@storybook/html'
import { useEffect } from '@storybook/client-api'
import Twig from 'twig';
import { setupTwig } from './setupTwig';
import once from '@drupal/once';

// GLOBAL CSS
import '../components/style.scss';

// If in a Drupal project, it's recommended to import a symlinked version of drupal.js.
import './_drupal.js';

// addDecorator deprecated, but not sure how to use this otherwise.
/*addDecorator((storyFn) => {
  useEffect(() => Drupal.attachBehaviors(), []);
  return storyFn();
});
*/
export const decorators = [
  (storyFn) => {
  useEffect(() => Drupal.attachBehaviors(), []);
  return storyFn();
}];

setupTwig(Twig);

const customViewports = {
  xs: {
    name: 'Extra Small',
    styles: {
      width: '375px',
      height: '100%',
    },
  },
  sm: {
    name: 'Small',
    styles: {
      width: '768px',
      height: '100%',
    },
  },
  md: {
    name: 'Medium',
    styles: {
      width: '992px',
      height: '100%',
    },
  },
  lg: {
    name: 'Large',
    styles: {
      width: '1200px',
      height: '100%',
    },
  },
  xl: {
    name: 'Extra Large',
    styles: {
      width: '1400px',
      height: '100%',
    },
  },
  xxl: {
    name: 'Silly Large',
    styles: {
      width: '1600px',
      height: '100%',
    },
  },
};

export const parameters = {
  status: {
    statuses: {
      released: {
        background: '#339999',
        color: '#ffffff',
        description: 'This component is stable and released',
      },
    },
  },
  controls: { expanded: true },
  actions: { argTypesRegex: '^on[A-Z].*' },
  options: {
    storySort: {
      method: 'alphabetical',
      order: ['Design Tokens', ['Introduction', '*', 'Others'], 'Base', 'Atoms', 'Molecules', 'Organisms', 'Templates', 'Pages'],
    },
  },
  viewport: {
    viewports: customViewports,
    defaultViewport: 'xl',
  },
  paddings: {
    values: [
      { name: 'None', value: '0' },
      { name: 'Small', value: '16px' },
      { name: 'Medium', value: '32px' },
      { name: 'Large', value: '64px' },
    ],
    default: 'Small',
  },
  designToken: {
    disable: true,
    styleInjection:
      '@import url("https://fonts.googleapis.com/css2?family=Merriweather:ital,wght@0,300;0,400;0,700;0,900;1,300;1,400;1,700;1,900&family=Open+Sans:ital,wght@0,300;0,400;0,500;0,600;0,700;0,800;1,300;1,400;1,500;1,600;1,700;1,800&display=swap");',
  },  
};
