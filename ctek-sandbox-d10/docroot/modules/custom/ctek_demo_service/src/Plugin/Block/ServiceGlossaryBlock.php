<?php

namespace Drupal\ctek_demo_service\Plugin\Block;

use Drupal\Core\Block\BlockBase;
use Drupal\node\Entity\Node;

/**
 * Provides a 'Service Glossary' Block.
 *
 * @Block(
 *   id = "service_glossary",
 *   admin_label = @Translation("Service Glossary"),
 *   category = @Translation("Service Glossary"),
 * )
 */
class ServiceGlossaryBlock extends BlockBase {

  /**
   * {@inheritdoc}
   */
  public function build() {
  	$tabs = [];
  	$letters = ['A','B','C','D','E','F','G','H','I',"J",'K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'];

  	foreach ($letters as $letter) {
  		$nodes = [];
  		$entity_ids = ServiceGlossaryBlock::getContentByLetter($letter);


  		if (!empty($entity_ids)) {
  			foreach ($entity_ids as $nid) {
      			array_push($nodes,$nid);
      		}
      	}

      	array_push($tabs, (object)[
	        'label' => $letter,
	        'nodes' => $nodes,
		]);
  	}

    return [
      '#tabs' => $tabs,	
      '#theme' => 'ctek_demo_service',
    ];
  }

  private function getContentByLetter($letter) {
  	$contentTypes = ['service','program','condition','treatment'];

  	$query = \Drupal::entityQuery('node')
  			->accessCheck(TRUE)
  			->condition('title', $letter . "%", 'LIKE')
  			->condition('type', $contentTypes, 'IN')
  			->sort('title','ASC');
  	$entity_ids = $query->execute();

  	return $entity_ids;
  }

}