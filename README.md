# nuxeo-generative-ai

This plugin for [Nuxeo Platform](http://hyland/com/nuxeo) allows for generating images from user's input text using OpenAI's DALL-E model.

## Configuration

Add to nuxeo.conf the following configurations and replace ${ORGANIZATION}/${API_KEY}:


```
generative.ai.openai.organization=${ORGANIZATION}
generative.ai.openai.apikey=${API_KEY}
```

Only `generative.ai.openai.apikey` is mandatory. The property `generative.ai.openai.organization` defaults to Nuxeo as an organization.

## Usage

By default, you can generate images in Folders and Workspaces by clicking the "Generate Image" button available in the
DOCUMENT_ACTIONS in the container.  The document types where this is enabled can be configured by setting the following property:

```
generative.ai.imagegenerator.containerDocTypes=Workspace,Folder
```

In order to generate the image you must set two parameters:
- Description: for example "dogs playing"
- Image type: can be "realistic" etc


After the image is generated you choose to regenerate or create a document with it. The type of the document that is created is configurable and defaults to `Picture`. You can change it by setting:

```generative.ai.imagegenerator.docType=Picture
```



## Build

```
git clone https://github.com/nuxeo-generative-ai.git
cd nuxeo-generative-ai
mvn clean install
```

####License

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

## About Nuxeo

Nuxeo Platform is an open source Content Services platform, written in Java.
Data can be stored in both SQL & NoSQL databases.

The development of the Nuxeo Platform is mostly done by Nuxeo employees with an
open development model.

The source code, documentation, roadmap, issue tracker, testing, benchmarks are
all public.

Typically, Nuxeo users build different types of information management solutions
for [document management](https://www.nuxeo.com/solutions/document-management/),
[case management](https://www.nuxeo.com/solutions/case-management/), and
[digital asset
management](https://www.nuxeo.com/solutions/dam-digital-asset-management/), use
cases. It uses schema-flexible metadata & content models that allows content to
be repurposed to fulfill future use cases.

More information is available at [hyland.com/nuxeo](https://hyland.com/nuxeo).
