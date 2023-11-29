import articleTwig from './article/article.twig';
import serviceTwig from './service/service.twig';
import conditionTwig from './service/condition.twig';


import breadcrumbData from '../../02-molecules/menus/breadcrumbs/breadcrumbs.yml';
import relatedLinksData from '../../02-molecules/related-links/related-links.yml';
import iconListData from '../../02-molecules/icon-list/icon-list.yml';
import quickLinksData from '../../02-molecules/quick-links/quick-links-bg.yml';
import textCardsData from '../../02-molecules/text-cards/text-cards.yml';
import testimonialData from '../../02-molecules/testimonial/testimonial-image.yml';
import heroCtasData from '../../02-molecules/hero-ctas/hero-ctas-row.yml';
import featuredMediaData from '../../02-molecules/featured-media/featured-media-video-embed.yml';
import articleHeroData from '../../03-organisms/hero/article/article.yml';
import serviceHeroData from '../../03-organisms/hero/service/service.yml';
import relatedMediaData from '../../02-molecules/related-media/related-media.yml';
import tagsData from '../../01-atoms/tags/tags.yml';

/**
 * Storybook Definition.
 */
export default {
  title: 'Pages/Content Types',
  parameters: {
    layout: 'fullscreen',
  },
};

export const article = () =>
  articleTwig({
    page_layout_modifier: 'contained',
    ...breadcrumbData,
    ...articleHeroData,
    ...tagsData,
    article_tags: [{'title': 'Lorem Ipsum'}],
    article_body: '<p> Skin cancer is the most common cancer in the United States. According to the American Academy of Dermatology, approximately 9,500 people in the United States are diagnosed with skin cancer every day. </p> <p> However, skin cancer is also one of the most preventable types of cancer. </p> <p> Monthly skin self-exams, awareness of warning signs, and early detection have been the best defense in the fight against skin cancer – helping to improve survival rates. However, skin cancer survival rates vary depending on the type of cancer diagnosed. </p> <p> <a href="https://healthcare.ascension.org/doctors/1457468654/yasmine-s-sido-franklin-wi">Yasmine S. Sido</a><em>, </em>PA-C, a physician assistant who specializes in medical and surgical dermatology at Ascension Medical Group Wisconsin shares how you can spot signs of skin cancer early. </p> <h2> &nbsp; </h2> <h2> <strong>Types of skin cancer and early detection</strong> </h2> <p> Here are some important things to keep in mind when it comes to some of the different types of skin cancer: </p> <p> There are three main types of skin cancer: </p> <ol> <li> <strong>Basal Cell Carcinoma</strong> is the most common form of skin cancer. It usually appears as a pearly, translucent raised nodule. The nodule can crust or bleed. </li> <li> <strong>Squamous Cell Carcinoma </strong>appears as a scaly patch or nodules. These cancers are most often found on sun-exposed areas like the face, lips, ears, neck and back of hands. </li> <li> <strong>Melanoma</strong> is the least common and fastest-growing form of skin cancer. Melanoma presents much like a mole that is asymmetrical, has an irregular border, varied coloring, diameter bigger than a pencil eraser and it evolves or changes over time. </li> </ol> <h2> &nbsp; </h2> <h2> <strong>Remember the ‘ABCs’ to help catch early signs of skin cancer</strong> </h2> <p> During a self-examination, pay attention to a change in the skin, a growth on the skin, a sore that doesn’t heal, and a change in a mole on the skin. These are all potential symptoms of skin cancer and should be brought to your healthcare provider’s attention. </p> <ul> <li> A – Asymmetrical: The shape of one half does not match the other. Is the mole or spot an irregular shape with two spots that look very different? </li> <li> B – Border. Are the edges of the mole or spot blurred, ragged, or irregular? </li> <li> C – Color. Is the mole or spot unevenly colored? If the color is uneven, it may include shades of black, brown, or tan. </li> <li> D – Diameter. Is the mole or spot larger than the size of a pea? Is the size of the mole/spot changing or growing larger? </li> <li> E – Evolving. Has the mole or spot changed over several weeks or months? </li> </ul> <h2> &nbsp; </h2> <h2> <strong>Tips to protect yourself in the sun&nbsp;</strong> </h2> <p> It’s never too late to start protecting your skin from the sun and indoor tanning beds. As soon as you do, your body starts to repair some of the damage caused by the UV rays. </p> <p> If you plan to spend time outside in the sun, physicians at Ascension Wisconsin recommend following these five tips to make sure you are protected at all times. </p> <ul> <li> Stay in the shade, especially between 10 a.m. and 4 p.m. </li> <li> Use sunscreen with sun protection of 30 or more, with UVA and UVB protection. </li> <li> Wear clothes that cover the arms and legs. </li> <li> Wear a hat that shades the face, head, ears and neck. </li> <li> Wear sunglasses that block UVA and UVB rays. </li> </ul> <h2> &nbsp; </h2> <h2> <strong>Talk to a doctor about a skin cancer screening</strong> </h2> <p> When you choose cancer centers and dermatology clinics at Ascension Wisconsin, you get a care team that is part of a national network. Our experienced dermatologists and cancer doctors share best practices and the latest in skin cancer treatment and research – bringing the best of cancer care to you. Talk with a doctor about the right timing for skin cancer screenings by calling <a href="tel:262-687-8677">262-687-8677</a><em>.</em> </p>'
  });

export const service = () =>
  serviceTwig({
    page_layout_modifier: 'contained',
    ...breadcrumbData,
    ...serviceHeroData,
    ...relatedLinksData,
    ...iconListData,
    ...heroCtasData,
    ...quickLinksData,
    ...testimonialData,
    ...relatedMediaData,
    service_body: "<h2>Our Approach to Cancer Care</h2><p>A cancer diagnosis changes everything. We know. And we also know how important timing is. The sooner you start treatment, the better your outcome.</p><p>At HOSPITAL, we'll go into action immediately — assembling a team that will be with you and your family every step of the way. And that starts on day one.</p><p>Here you'll find the advanced care, compassion and support you need, all from specialized teams dedicated to guiding you through diagnosis, treatment and recovery.</p><p>As one of the leading academic health systems in the nation, HOSPITAL offers the most advanced cancer care, including immunotherapies, targeted therapies and minimally invasive surgical options. You’ll also have access to clinical trials that are looking at novel approaches to treating a wide range of cancers.</p><ul><li><strong>Aenean:</strong> Euismod bibendum laoreet.</li><li><strong>Proin gravida: </strong>Dolor sit amet lacus accumsan et viverra justo commodo.</li><li><strong>Nam fermentum:</strong> Nulla luctus pharetra vulputate, felis tellus mollis orci.</li><li><strong>Sed rhoncus:</strong> Sapien nunc eget odio lorem ipsum dolor sit amet.</li></ul>",
  });

  export const condition = () =>
  conditionTwig({
    page_layout_modifier: 'contained',
    ...breadcrumbData,
    ...serviceHeroData,
    ...relatedLinksData,
    ...iconListData,
    ...heroCtasData,
    ...textCardsData,
    ...testimonialData,
    ...featuredMediaData,
    ...relatedMediaData,
    service_body: "<h2>Our Approach to Cancer Care</h2><p>A cancer diagnosis changes everything. We know. And we also know how important timing is. The sooner you start treatment, the better your outcome.</p><p>At HOSPITAL, we'll go into action immediately — assembling a team that will be with you and your family every step of the way. And that starts on day one.</p><p>Here you'll find the advanced care, compassion and support you need, all from specialized teams dedicated to guiding you through diagnosis, treatment and recovery.</p><p>As one of the leading academic health systems in the nation, HOSPITAL offers the most advanced cancer care, including immunotherapies, targeted therapies and minimally invasive surgical options. You’ll also have access to clinical trials that are looking at novel approaches to treating a wide range of cancers.</p><ul><li><strong>Aenean:</strong> Euismod bibendum laoreet.</li><li><strong>Proin gravida: </strong>Dolor sit amet lacus accumsan et viverra justo commodo.</li><li><strong>Nam fermentum:</strong> Nulla luctus pharetra vulputate, felis tellus mollis orci.</li><li><strong>Sed rhoncus:</strong> Sapien nunc eget odio lorem ipsum dolor sit amet.</li></ul>",
  });
