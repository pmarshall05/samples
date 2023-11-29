<?php

namespace Drupal\ctek_demo_service\Controller;
use Drupal\config_pages\Entity\ConfigPages;
use Drupal\Core\Controller\ControllerBase;
use Drupal\Core\Url;

/**
* Provides route responses for the Welcome page module.
*/
class CtekDemoServiceController extends ControllerBase {
 
  public function index() {
   $configPage = ConfigPages::config('services');
   $build = [];

    if ($configPage !== NULL) {
      $build = \Drupal::service('entity_type.manager')
        ->getViewBuilder('config_pages')
        ->view($configPage);

      $build['#cache']['tags'][] = 'services';
    }
    
    return $build;
  }
}